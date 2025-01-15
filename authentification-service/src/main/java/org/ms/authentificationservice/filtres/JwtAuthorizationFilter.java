package org.ms.authentificationservice.filtres;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

//Classe de filtre utilisé pour intercepter toute requête demandant une ressource
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	public final String PREFIXE_JWT = "Bearer ";
	public final String CLE_SIGNATURE = "MaClé";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
// récupérer le header "Authorization"
		String authorizationToken = request.getHeader("Authorization");
// vérifier l'état du header
		if (authorizationToken != null && authorizationToken.startsWith(PREFIXE_JWT)) {
			try {
//récupérer la valeur du JWT
				String jwt = authorizationToken.substring(PREFIXE_JWT.length());
//Préparer une instance du même algorithme de cryptage (HMAC256)
				Algorithm algo = Algorithm.HMAC256(CLE_SIGNATURE);
// vérifier la validité du JWT par la vérification de sa signature
				JWTVerifier jwtVerifier = JWT.require(algo).build();
//décoder le JWT
				DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
//récupérer les données du JWT
				String username = decodedJWT.getSubject();
				String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
				Collection<GrantedAuthority> permessions = new ArrayList<>();
				for (String r : roles) {
					permessions.add(new SimpleGrantedAuthority(r));
				}
				// construire une authentication
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username, null, permessions);
				// authentifier l'utilisateur pour la requête actuelle
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				// passer tu es reconnu
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				response.setHeader("error-message", e.getMessage());
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		} else {
			// passer mais non reconnu
			filterChain.doFilter(request, response);
		}
	}
}