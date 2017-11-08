package auth

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/auth/code/$provider" ( controller:'auth', action:'code' )
        "/"(controller:'auth', action:'index')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
