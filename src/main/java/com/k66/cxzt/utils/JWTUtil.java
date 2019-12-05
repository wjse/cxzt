package com.k66.cxzt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

public class JWTUtil {
	private static final long EXPIRATION = 3600L;
	private static final long EXPIRATION_REMEMBER = 604800L;
	private static final long MINUTES_10 = 10 * 60 * 1000;

	private static final String SECRET = "cxzt!@#$";
	private static final String ISS = "cxztcnpc";

	public static String createToken(String id , String username , boolean isRe){
		long expiration = isRe ? EXPIRATION_REMEMBER : EXPIRATION;
		return Jwts.builder()
						   .signWith(SignatureAlgorithm.HS512 , SECRET)
						   .setIssuer(ISS)
						   .setId(id)
						   .setSubject(username)
						   .setIssuedAt(new Date())
						   .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
						   .compact();

	}

	public static boolean isExpire(String token){
		Claims claims = getTokenBody(token);
		return null == claims ? true : claims.getExpiration().before(new Date());
	}

	public static boolean isWillExpire(String token){
		Claims claims = getTokenBody(token);
		if(null == claims){
			return true;
		}

		return (claims.getExpiration().getTime() - System.currentTimeMillis()) < MINUTES_10;
	}

	public static Pair<String , String> get(String token){
		Claims claims = getTokenBody(token);
		return null == claims ? null : Pair.of(claims.getId() , claims.getSubject());
	}

	private static Claims getTokenBody(String token){
		try {
			return Jwts.parser()
							.setSigningKey(SECRET)
							.parseClaimsJws(token)
							.getBody();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
