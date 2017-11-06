package co.semweb.auth

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class SwuserSwrole implements Serializable {

	private static final long serialVersionUID = 1

	Swuser swuser
	Swrole swrole

	@Override
	boolean equals(other) {
		if (other instanceof SwuserSwrole) {
			other.swuserId == swuser?.id && other.swroleId == swrole?.id
		}
	}

    @Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (swuser) {
            hashCode = HashCodeHelper.updateHash(hashCode, swuser.id)
		}
		if (swrole) {
		    hashCode = HashCodeHelper.updateHash(hashCode, swrole.id)
		}
		hashCode
	}

	static SwuserSwrole get(long swuserId, long swroleId) {
		criteriaFor(swuserId, swroleId).get()
	}

	static boolean exists(long swuserId, long swroleId) {
		criteriaFor(swuserId, swroleId).count()
	}

	private static DetachedCriteria criteriaFor(long swuserId, long swroleId) {
		SwuserSwrole.where {
			swuser == Swuser.load(swuserId) &&
			swrole == Swrole.load(swroleId)
		}
	}

	static SwuserSwrole create(Swuser swuser, Swrole swrole, boolean flush = false) {
		def instance = new SwuserSwrole(swuser: swuser, swrole: swrole)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(Swuser u, Swrole r) {
		if (u != null && r != null) {
			SwuserSwrole.where { swuser == u && swrole == r }.deleteAll()
		}
	}

	static int removeAll(Swuser u) {
		u == null ? 0 : SwuserSwrole.where { swuser == u }.deleteAll() as int
	}

	static int removeAll(Swrole r) {
		r == null ? 0 : SwuserSwrole.where { swrole == r }.deleteAll() as int
	}

	static constraints = {
	    swuser nullable: false
		swrole nullable: false, validator: { Swrole r, SwuserSwrole ur ->
			if (ur.swuser?.id) {
				if (SwuserSwrole.exists(ur.swuser.id, r.id)) {
				    return ['userRole.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['swuser', 'swrole']
    table 'sw_user_role'
    swuser column: 'swur_user_fk'
    swrole column: 'swur_role_fk'
		version false
	}
}
