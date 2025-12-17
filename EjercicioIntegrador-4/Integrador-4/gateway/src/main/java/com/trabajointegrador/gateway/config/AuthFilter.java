package com.trabajointegrador.gateway.config;

import com.trabajointegrador.gateway.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final JwtUtils jwtUtils;

    public AuthFilter(JwtUtils jwtUtils) {
        super(Config.class);
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 1. Verificar Header Authorization activa el filtro y revisa token si viene
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authHeader.replace("Bearer ", "");
            //extrae token
            // 2. Validar firma del Token(que halla sido generado por mi micro)
            if (!jwtUtils.validateToken(token)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
                /*
                El Gateway intenta abrir el token usando la misma clave secreta que usa
                el micro de usuarios.
                Si el token NO fue generado con esa clave → la firma no
                 coincide → salta excepción.
                 */
            }

            // 3. Validar ROL (Si la ruta lo pide en el YAML)
            if (config.role != null) {
                Claims claims = jwtUtils.getClaims(token);
                //si la firma es valida abre el token y lee los claims
                Object rolesObject = claims.get("auth");
                String rolesString = String.valueOf(rolesObject);

                // NUEVO CÓDIGO DE DIAGNÓSTICO
                System.out.println("---------------------------------");
                System.out.println("YAML (config.role) requiere: " + config.role);
                System.out.println("TOKEN (rolesString) lee: " + rolesString);
                System.out.println("El Token contiene el rol requerido?: " + rolesString.contains(config.role));
                System.out.println("---------------------------------");
                // FIN DEL CÓDIGO DE DIAGNÓSTICO


                // Si el rol requerido NO está en el token -> 403 FORBIDDEN
                if (!rolesString.contains(config.role)) {
                    return onError(exchange, HttpStatus.FORBIDDEN);
                }

            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
        private String role;
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}