package modelo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Product {
    private int id;
    private  String nombre;
    private double precio;
    private LocalDateTime fecha_registro;
}
