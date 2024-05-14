package com.example.rna.exam.ia;

import com.example.rna.exam.ia.entity.ParametrosEntrenamiento;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RNA {
    int[][] matriz = new int[21][63]; // Crear una matriz de 21 filas y 63 columnas
    double factorA;
    double bias;
    double weightss[];
    int epocaa;
    int[][] etiquetas = {
            {1,0,0,0,0,0,0},
            {0,1,0,0,0,0,0},
            {0,0,1,0,0,0,0},
            {0,0,0,1,0,0,0},
            {0,0,0,0,1,0,0},
            {0,0,0,0,0,1,0},
            {0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0},
            {0,1,0,0,0,0,0},
            {0,0,1,0,0,0,0},
            {0,0,0,1,0,0,0},
            {0,0,0,0,1,0,0},
            {0,0,0,0,0,1,0},
            {0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0},
            {0,1,0,0,0,0,0},
            {0,0,1,0,0,0,0},
            {0,0,0,1,0,0,0},
            {0,0,0,0,1,0,0},
            {0,0,0,0,0,1,0},
            {0,0,0,0,0,0,1}
    };

    public int[][] leerArchivo(String contenido) {
        System.out.println("Contenido del archivo:");
        System.out.println(contenido);

        int fila = 0;
        int columna = 0;

        for (int i = 0; i < contenido.length() && fila < 21; i++) {
            char caracter = contenido.charAt(i);
            if (caracter == '#' || caracter == '@') {
                matriz[fila][columna] = 1;
            } else if (caracter == '.' || caracter == 'o') {
                matriz[fila][columna] = -1;
            }

            if (caracter != '\n' && caracter != '\r') { // Ignorar los saltos de línea
                columna++;
            }

            if (columna == 63) { // Si se alcanza la columna 63, pasar a la siguiente fila
                fila++;
                columna = 0;
            }
        }

        System.out.println("Matriz resultante:");
        imprimirMatriz(matriz);

        return matriz;
    }

    private void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }
    public double fNet(int fila[], double[] weights, double bias) {
        // Inicializar la suma
        double suma = 0;

        // Multiplicar cada elemento de la fila por su peso correspondiente y sumarlo a la suma
        for (int i = 0; i < fila.length; i++) {
            suma += fila[i] * weights[i]; // Multiplicar el valor de la fila con el peso correspondiente y sumarlo
        }

        // Sumar el sesgo multiplicado por el peso correspondiente
        suma += bias;

        // Retornar el resultado
        return suma;
    }

    public String entrenar(ParametrosEntrenamiento parametrosEntrenamiento) {
        StringBuilder trainingResult = new StringBuilder();
        trainingResult.append("Training Results:\n");

        for (int epoch = 0; epoch < parametrosEntrenamiento.getEpocas(); epoch++) {
            int correctPredictions = 0;

            for (int i = 0; i < matriz.length; i++) {
                double result = fNet(matriz[i], parametrosEntrenamiento.getWeights(), parametrosEntrenamiento.getBias());
                int prediction = result > 0 ? 1 : 0;

                if (prediction == etiquetas[i][Math.min(epoch, etiquetas[i].length - 1)]) {
                    correctPredictions++;
                }

                if (epoch < etiquetas[i].length && prediction != etiquetas[i][epoch]) {
                    for (int j = 0; j < parametrosEntrenamiento.getWeights().length; j++) {
                        parametrosEntrenamiento.getWeights()[j] += parametrosEntrenamiento.getAlpha() * (etiquetas[i][epoch] - prediction) * matriz[i][j];
                    }
                    parametrosEntrenamiento.setBias(parametrosEntrenamiento.getBias() + parametrosEntrenamiento.getAlpha() * (etiquetas[i][epoch] - prediction));
                }
            }

            double learningPercentage = (double) correctPredictions / matriz.length * 100;

            // Imprimir los resultados de la época actual
            trainingResult.append(String.format("Epoch %d: Learning Percentage: %.1f%%, Weights: %s, Bias: %.2f\n", epoch + 1, learningPercentage, Arrays.toString(parametrosEntrenamiento.getWeights()), parametrosEntrenamiento.getBias()));
        }

        // Asignar valores actualizados a las variables al final del entrenamiento
        weightss = parametrosEntrenamiento.getWeights();
        bias = parametrosEntrenamiento.getBias();
        factorA = parametrosEntrenamiento.getAlpha();
        epocaa = parametrosEntrenamiento.getEpocas();

        // Retornar la cadena con todos los resultados
        return trainingResult.toString();
    }

    public int predecir(int fila[], double[] weights, double bias) {
        double resultado = fNet(fila, weights, bias);

        return resultado > 0 ? 1 : 0;
    }
    public String test() {
        StringBuilder testResult = new StringBuilder();
        testResult.append("Test Results:\n");

        // Copiar los pesos originales para su uso en el test
        double[] originalWeights = Arrays.copyOf(weightss, weightss.length);

        for (int epoch = 0; epoch < 1; epoch++) {
            int correctPredictions = 0;

            for (int i = 0; i < matriz.length; i++) {
                // Calcular la salida utilizando los pesos originales
                double result = fNet(matriz[i], originalWeights, bias);

                // Aplicar la función de activación
                int prediction = result > 0 ? 1 : 0;

                // Comprobar si la predicción es correcta
                if (prediction == etiquetas[i][Math.min(epoch, etiquetas[i].length - 1)]) {
                    correctPredictions++; // Incrementar el contador si la predicción es correcta
                }

                // No actualizar los pesos en el test
            }

            double learningPercentage = (double) correctPredictions / matriz.length * 100;
            testResult.append(String.format("Epoch %d: Learning Percentage: %.1f%%, Weights: %s, Bias: %.2f\n", epoch + 1, learningPercentage, Arrays.toString(originalWeights), bias));
        }

        // Retornar la cadena con todos los resultados
        return testResult.toString();
    }


}