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
