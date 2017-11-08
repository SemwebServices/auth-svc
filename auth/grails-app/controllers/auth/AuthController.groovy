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

    if ( ( params.provider ) && ( grailsApplication.config.authProviders[params.provider] ) {
      def oidc_cfg = grailsApplication.config.authProviders[params.provider]
      log.debug("Processing auth request for provider ${params.provider} : ${oidc_cfg}");
      def state=java.util.UUID.randomUUID();
      def nonce=java.util.UUID.randomUUID();
      redirect(url:"${oidc_cfg.authorization_endpoint}?client_id=${oidc_cfg.clientId}"+
                            '&response_type=code'+
                            '&scope=openid%20email'+
                            '&redirect_uri=http://localhost:8080/oidcCallback'+
                            "&state=${state}"+
                            "&nonce=${nonce}")
    }
    log.debug("AuthController::index(${params})");
    if ( params.redirect ) {
      log.debug("Redirecting to ${params.redirect}");
      redirect(url:params.redirect)
    }
    else {
      log.debug("No redirect -- continue");
    }
    result;
  }
}
