package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.entities.Consulta;
import com.gestion.emprendedora.entities.InformeSesion;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.ConsultaRepository;
import com.gestion.emprendedora.repositories.InformeSesionRepository;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import com.gestion.emprendedora.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/informes")
public class InformeController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private InformeSesionRepository informeRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/{informeId}/download")
    public ResponseEntity<byte[]> downloadInformePdf(@PathVariable Long informeId) {
        // Obtenemos todos los datos necesarios
        InformeSesion informe = informeRepository.findById(informeId).orElseThrow();
        Consulta consulta = consultaRepository.findById(informe.getIdConsulta()).orElseThrow();
        Usuario estudiante = usuarioRepository.findById(consulta.getIdEstudiante()).orElseThrow();
        Usuario docente = usuarioRepository.findById(informe.getIdDocente()).orElseThrow();

        byte[] pdfContents = pdfService.generarInformePdf(informe, estudiante, docente, consulta);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "Informe_Progreso_" + estudiante.getNombreCompleto().replace(" ", "_") + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok().headers(headers).body(pdfContents);
    }
}