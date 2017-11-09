package co.semweb.auth

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class Swuser implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password
    String email
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    String issuer
    String issuerId

    Set<Swrole> getAuthorities() {
        (SwuserSwrole.findAllBySwuser(this) as List<SwuserSwrole>)*.swrole as Set<Swrole>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
           email nullable: true, blank: false, unique: false
          issuer nullable: true, blank: false, unique: false
        issuerId nullable: true, blank: false, unique: true
    }

    static mapping = {
      table 'sw_user'
                   id column: 'swu_id'
             username column: 'swu_user'
             password column: 'swu_pass'
               issuer column: 'swu_issuer'
             issuerId column: 'swu_issuer_id'
	            enabled column: 'swu_enabled'
	     accountExpired column: 'swu_ac_expired'
	      accountLocked column: 'swu_locked'
	    passwordExpired column: 'swu_pw_expired'
    }
}
