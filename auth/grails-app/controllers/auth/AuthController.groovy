package auth

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.*
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.GET
import org.jose4j.jwk.*
import org.jose4j.jwt.*
import org.jose4j.jws.*
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;


/**
 * Authorize the selected user to access the identifed application data.
 */


class AuthController {

   def springSecurityService

  /**
   * This method is secured, so the user will be prompted to log in if not already authenticated.
   */
  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def prompt() { 
    def result = [:]
    log.debug("AuthController::index(${params})");
    result.authProviders = grailsApplication.config.authProviders;

  }

  def oidcAuth() {
    if ( ( params.provider ) && ( grailsApplication.config.authProviders[params.provider].type='oidc' ) ) {
      def oidc_cfg = grailsApplication.config.authProviders[params.provider]
      log.debug("Processing auth request for provider ${params.provider} : ${oidc_cfg}");
      def state=java.util.UUID.randomUUID();
      def nonce=java.util.UUID.randomUUID();
      redirect(url:"${oidc_cfg.authorization_endpoint}?client_id=${oidc_cfg.clientId}"+
                            '&response_type=code'+
                            '&scope=openid%20email'+
                            "&redirect_uri=http://localhost:8080/auth/code"+
                            "&state=${state}"+
                            "&nonce=${nonce}")
    }

    result;
  }

  /**
   * Handle code response from an oidc provider authorize request
   */
  def code() {
    def result = [:]
    def user_token = null;

    log.debug("AuthController::code ${params}");
    // example response:: http://localhost:8080/auth/code?state=f7a30749-6697-4037-9b71-11bd61003729&code=4/.AABMWQrBBQV3lzYpYZmPJ-WWWW7-j6B0ehhZASLCwdB5gENLobRUPfEmfIjRktmxv_Ya46dE3SVA8UIJwQnKzwE&authuser=2&hd=semweb.co&session_state=f8208bc9aa0d91f68a598cdd9751269216fce216..2630&prompt=consent#

    // We are now at step 4 from https://developers.google.com/identity/protocols/OpenIDConnect
    // 4. Exchange code for access token and ID token
    if ( ( params.provider ) && ( grailsApplication.config.authProviders[params.provider].type='oidc' ) ) {
      def oidc_cfg = grailsApplication.config.authProviders[params.provider]
      // def token_req = oidc_cfg.token_endpoint+"code=${params.code}
      def tokenUri = oidc_cfg.token_endpoint.toURI()

      def http = new HTTPBuilder(tokenUri.scheme + "://" + tokenUri.host)

      log.debug("About to call post on ${oidc_cfg.token_endpoint} / ${tokenUri.scheme}:${tokenUri.host}:${tokenUri.path} -- to validate token")

      def access_params = [
        code: params.code,
        client_id: oidc_cfg.clientId,
        client_secret: oidc_cfg.clientSecret,
        redirect_uri: 'http://localhost:8080/auth/code/'+params.provider,
        grant_type: 'authorization_code'
      ];

      log.debug("Access params: ${access_params}");

      http.post( path:tokenUri.path, 
                 body:access_params,
                 requestContentType: 'application/x-www-form-urlencoded; charset=utf-8') { resp, json ->
        log.debug("POST Success: ${resp} ${json}")

        def user_info = decodeJwt(oidc_cfg.jwks_uri, oidc_cfg.clientId, json.id_token)
        user_token = json.id_token;

        log.debug("User Info: ${user_info}");
      }
    }

    // claim.iss + claim.sub -- issuer + subscriber == a globally unique id
    redirect(url:"http://localhost:3000/callback#"+user_token);

    result
  }


  private String createToken(userid) {

    // See https://bitbucket.org/b_c/jose4j/wiki/JWT%20Examples

    RsaJsonWebKey rsaJsonWebKey = publicKeyService.getAppPublicKey()
    // log.debug("Got app public key ${rsaJsonWebKey}");
    // RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
    // Give the JWK a Key ID (kid), which is just the polite thing to do
    // rsaJsonWebKey.setKeyId("k1");

    // Create the Claims, which will be the content of the JWT
    JwtClaims claims = new JwtClaims();
    claims.setIssuer("semweb.co");  // who creates the token and signs it
    claims.setAudience("semweb.co"); // to whom the token is intended to be sent
    claims.setExpirationTimeMinutesInTheFuture(60*60); // time when the token will expire (60*60 minutes from now)
    claims.setGeneratedJwtId(); // a unique identifier for the token
    claims.setIssuedAtToNow();  // when the token was issued/created (now)
    claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
    claims.setSubject(userid); // the subject/principal is whom the token is about
    // claims.setClaim("email","mail@example.com"); // additional claims/attributes about the subject can be added
    // List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
    // claims.setStringListClaim("groups", groups); // multi-valued claims work too and will end up as a JSON array

    // A JWT is a JWS and/or a JWE with JSON claims as the payload.
    // In this example it is a JWS so we create a JsonWebSignature object.
    JsonWebSignature jws = new JsonWebSignature();

    // The payload of the JWS is JSON content of the JWT Claims
    jws.setPayload(claims.toJson());

    // The JWT is signed using the private key
    jws.setKey(rsaJsonWebKey.getPrivateKey());

    // Set the Key ID (kid) header because it's just the polite thing to do.
    // We only have one key in this example but a using a Key ID helps
    // facilitate a smooth key rollover process
    jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

    // Sign the JWS and produce the compact serialization or the complete JWT/JWS
    // representation, which is a string consisting of three dot ('.') separated
    // base64url-encoded parts in the form Header.Payload.Signature
    // If you wanted to encrypt it, you can simply set this jwt as the payload
    // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
    String jwt = jws.getCompactSerialization();

    // log.debug("Created jwt : ${jwt}")
    return jwt
  }

  private def decodeJwt(jwks_api, client_id, token) {
    log.debug("decodeJwt(${jwks_api},...)");

    HttpsJwks httpsJkws = new HttpsJwks(jwks_api);

    HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);

    def jwtConsumer = new JwtConsumerBuilder()
            .setVerificationKeyResolver(httpsJwksKeyResolver)
            .setExpectedAudience(client_id)
            .build();

    //  Validate the JWT and process it to the Claims
    JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
    log.debug("JWT validation succeeded! " + jwtClaims);

    jwtClaims
  }
}
