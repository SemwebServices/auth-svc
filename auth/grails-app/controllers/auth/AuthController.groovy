package auth

import grails.plugin.springsecurity.annotation.Secured

/**
 * Authorize the selected user to access the identifed application data.
 */


class AuthController {

   def springSecurityService

  /**
   * This method is secured, so the user will be prompted to log in if not already authenticated.
   */
  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
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
    log.debug("AuthController::code");
    // example response:: http://localhost:8080/auth/code?state=f7a30749-6697-4037-9b71-11bd61003729&code=4/.AABMWQrBBQV3lzYpYZmPJ-WWWW7-j6B0ehhZASLCwdB5gENLobRUPfEmfIjRktmxv_Ya46dE3SVA8UIJwQnKzwE&authuser=2&hd=semweb.co&session_state=f8208bc9aa0d91f68a598cdd9751269216fce216..2630&prompt=consent#

    result
  }
}
