package auth

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/auth/code/$provider" ( controller:'auth', action:'code' )
        "/"(view:'/index.gsp')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
