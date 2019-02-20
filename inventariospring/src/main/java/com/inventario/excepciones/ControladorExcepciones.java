package com.inventario.excepciones;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControladorExcepciones {
   @ExceptionHandler(value = ExcepcionesPersonalizadas.class)
   public ResponseEntity<Object> exception(ExcepcionesPersonalizadas exception) {
      return new ResponseEntity<>("Objeto no encontrado!", HttpStatus.NOT_FOUND);
   }
}