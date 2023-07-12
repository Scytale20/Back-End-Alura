package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
public class MedicosController {

    @Autowired
    private MedicoRepository medicoRepository;
    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico, UriComponentsBuilder uriComponentsBuilder){
         Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
         DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                 medico.getTelefono(), medico.getEspecialidad().toString(),
                 new DatosDireccion(medico.getDirecion().getCalle(),
                         medico.getDirecion().getDistrito(),
                         medico.getDirecion().getCiudad(),
                         medico.getDirecion().getNumero(),
                         medico.getDirecion().getComplemento()));
         URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
         return ResponseEntity.created(url).body(datosRespuestaMedico);

    }
    @GetMapping
    public ResponseEntity<Page<DatosListadoMedico>>  listadoMedicos(@PageableDefault(size = 2) Pageable paginacion){
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDirecion().getCalle(),
                        medico.getDirecion().getDistrito(),
                        medico.getDirecion().getCiudad(),
                        medico.getDirecion().getNumero(),
                        medico.getDirecion().getComplemento())));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        //DELETE LOGICO
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();

        //DELETE EN BASE DE DATOS
        //Medico medico = medicoRepository.getReferenceById(id);
        //medicoRepository.delete(medico);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retornaDatosMedico(@PathVariable Long id) {

        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDirecion().getCalle(),
                        medico.getDirecion().getDistrito(),
                        medico.getDirecion().getCiudad(),
                        medico.getDirecion().getNumero(),
                        medico.getDirecion().getComplemento()));

        return ResponseEntity.ok(datosMedico);
    }

}
