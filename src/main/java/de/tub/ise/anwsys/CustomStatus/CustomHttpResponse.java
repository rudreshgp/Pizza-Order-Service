package de.tub.ise.anwsys.CustomStatus;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomHttpResponse {
	public static <T> ResponseEntity<T> createResponse(HttpStatus status, String headerName, String headerDescription, T body) {
		return ResponseEntity.status(status)
				.header(headerName, headerDescription)
				.body(body);
	}

	public static <T> ResponseEntity<T> createResponse(HttpStatus status, T body) {
		return ResponseEntity.status(status)
				.body(body);
	}
}
