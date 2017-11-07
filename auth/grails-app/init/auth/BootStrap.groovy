package auth

class BootStrap {

  def grailsApplication

  def init = { servletContext ->
    log.debug("Auth service init");
    log.info("Sys id: ${grailsApplication.config.systemId}")
  }

  def destroy = {
  }
}
