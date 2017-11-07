package auth

import co.semweb.auth.*;

class BootStrap {

  def grailsApplication

  def init = { servletContext ->
    log.debug("Auth service init");
    log.info("Sys id: ${grailsApplication.config.systemId}")

    def role_user = Swrole.findByAuthority('ROLE_USER') ?: new Swrole(authority:'ROLE_USER').save(flush:true, failOnError:true);
    def role_adm = Swrole.findByAuthority('ROLE_ADMIN') ?: new Swrole(authority:'ROLE_ADMIN').save(flush:true, failOnError:true);

    def adm_user = Swuser.findByUsername('admin')

    if ( adm_user == null ) {

      log.debug("Bootstrapping admin user");

      adm_user = new Swuser(username:'admin',
                            password:'admin').save(flush:true, failOnError:true);

      SwuserSwrole.create(adm_user, role_user, true);
      SwuserSwrole.create(adm_user, role_adm, true);
    }
  }

  def destroy = {
  }
}
