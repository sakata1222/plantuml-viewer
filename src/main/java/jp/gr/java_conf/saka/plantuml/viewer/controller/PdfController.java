package jp.gr.java_conf.saka.plantuml.viewer.controller;

import java.io.IOException;
import java.net.URI;
import jp.gr.java_conf.saka.plantuml.viewer.controller.vo.UmlRequest;
import jp.gr.java_conf.saka.plantuml.viewer.controller.vo.UmlResponse;
import jp.gr.java_conf.saka.plantuml.viewer.service.IPlantUmlPdfService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pdf")
public class PdfController {

  private IPlantUmlPdfService plantUmlService;

  public PdfController(
    IPlantUmlPdfService plantUmlService) {
    this.plantUmlService = plantUmlService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UmlResponse> post(
    @RequestBody UmlRequest request,
    BindingResult result,
    UriComponentsBuilder urlBuilder)
    throws IOException {
    if (result.hasErrors()) {
      throw new IllegalArgumentException(result.getFieldError().toString());
    }
    String id = plantUmlService.createPdf(request.getPlantUml());
    String path = "pdf/" + id + "/raw.pdf";
    URI rawDataUri = urlBuilder.path(path).build().toUri();
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(
        UmlResponse.builder()
          .id(id)
          .plantUml(request.getPlantUml())
          .rawDataPath(path)
          .rawDataUrl(rawDataUri)
          .build()
      );
  }

  @GetMapping(value = "{id}/raw.pdf")
  public ResponseEntity<Resource> rawData(@PathVariable("id") String id) {
    return plantUmlService.findPdf(id).map(
      bytes ->
        ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_PDF)
          .<Resource>body(new ByteArrayResource(bytes)))
      .orElseThrow(() -> new IllegalArgumentException(id));
  }

}
