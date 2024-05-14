package com.example.rna.exam.ia.controller;

import com.example.rna.exam.ia.entity.ParametrosEntrenamiento;
import com.example.rna.exam.ia.RNA;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rna")
@CrossOrigin(origins = "http://localhost:4200")
public class RNAController {
    private final RNA readerText;
    @PostMapping("/upload")
    public int[][] sendFile(@RequestBody String contenido){
        return readerText.leerArchivo(contenido);
    }

    @PostMapping("/entrenamiento")
    public ResponseEntity<Map<String, Object>> makeTrain(@RequestBody ParametrosEntrenamiento parametros) {
        // Procesar los parámetros y obtener los resultados
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Entrenamiento exitoso");
        response.put("results", readerText.entrenar(parametros)/* Coloca aquí los resultados obtenidos */);

        // Devolver la respuesta como un objeto JSON
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/predecir")
    public ResponseEntity<Map<String, Object>> makeTest() {
        // Procesar los parámetros y obtener los resultados
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Entrenamiento exitoso");
        response.put("results", readerText.test()/* Coloca aquí los resultados obtenidos */);

        // Devolver la respuesta como un objeto JSON
        return ResponseEntity.ok().body(response);
    }
}
