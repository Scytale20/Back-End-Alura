package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;
@Table(name="medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String documento;
    private Boolean activo;
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    @Embedded
    private Direccion direcion;

    public Medico(DatosRegistroMedico datosRegistroMedicoMedico) {
        this.activo = true;
        this.nombre = datosRegistroMedicoMedico.nombre();
        this.email = datosRegistroMedicoMedico.email();
        this.documento = datosRegistroMedicoMedico.documento();
        this.telefono = datosRegistroMedicoMedico.telefono();
        this.especialidad = datosRegistroMedicoMedico.especialidad();
        this.direcion = new Direccion(datosRegistroMedicoMedico.direccion());
    }

    public void actualizarDatos(DatosActualizarMedico datosActualizarMedico) {
        if(datosActualizarMedico.nombre() != null){
            this.nombre = datosActualizarMedico.nombre();
        }
        if(datosActualizarMedico.documento() != null){
            this.documento = datosActualizarMedico.documento();
        }
        if(datosActualizarMedico.direccion() != null){
            this.direcion = direcion.actualizarDatos(datosActualizarMedico.direccion());
        }



    }

    public void desactivarMedico() {
        this.activo = false;
    }
}
