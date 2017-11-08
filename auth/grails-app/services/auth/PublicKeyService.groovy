package auth

import org.apache.commons.codec.binary.Base64
import org.jose4j.jwk.*
import org.jose4j.jwt.*
import org.jose4j.jwt.consumer.*
import org.jose4j.jws.*


class PublicKeyService {

  private RsaJsonWebKey thekey = null;
  def grailsApplication

  @javax.annotation.PostConstruct
  def init() {
    log.debug("Init");
  }

  def getAppPublicKey() {
    // See if the app has a public key, if not generate one and store it
    if ( thekey == null ) {
      log.debug("Creating public key");

      Map<String, Object> keyparams = grailsApplication.config.jwk
      thekey = new org.jose4j.jwk.RsaJsonWebKey(keyparams)
    }

    return thekey;
  }

  def decodeJWT(jwt) {

    JwtClaims result = null

    def rsaJsonWebKey = getAppPublicKey();


    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
            .setRequireExpirationTime() // the JWT must have an expiration time
            .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
            .setRequireSubject() // the JWT must have a subject claim
            .setExpectedIssuer("semweb.co")  // who creates the token and signs it
            .setExpectedAudience("semweb.co") // to whom the token is intended to be sent
            .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
            .build(); // create the JwtConsumer instance

    try
    {
        //  Validate the JWT and process it to the Claims
        result = jwtConsumer.processToClaims(jwt);
        //log.debug("JWT validation succeeded! " + result);
    }
    catch (InvalidJwtException e)
    {
        // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
        // Hopefully with meaningful explanations(s) about what went wrong.
        log.error("Invalid JWT! " + e);
    }

    result
  }


}
