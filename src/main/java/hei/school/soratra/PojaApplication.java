package hei.school.soratra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@PojaGenerated
public class PojaApplication {

  public static void main(String[] args) {
    SpringApplication.run(PojaApplication.class, args);
  }

  @RestController
  @Getter
  @Setter
  public class SoratraController {

    @Autowired
    private SoratraRepository soratraRepository;

    @PutMapping("/soratra/{id}")
    public ResponseEntity<?> putSoratra(@PathVariable Long id, @RequestBody String phrase) {
      if (phrase == null || phrase.isEmpty()) {
        return ResponseEntity.badRequest().body("Empty phrase provided");
      }

      String phraseMinuscule = phrase.toLowerCase();
      Soratra soratra = new Soratra(id, phraseMinuscule);
      try {
        soratraRepository.save(soratra);
        return ResponseEntity.ok().build();
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error saving phrase: " + e.getMessage());
      }
    }

    @GetMapping("/soratra/{id}")
    public ResponseEntity<?> getSoratra(@PathVariable Long id) {
      Soratra soratra = soratraRepository.findById(id).orElse(null);
      if (soratra == null) {
        return ResponseEntity.notFound().build();
      }

      String originalUrl = getPresignedUrl(soratra.getOriginalFilename());
      String transformedUrl = getPresignedUrl(soratra.getTransformedFilename());

      Map<String, String> responseBody = new HashMap<>();
      responseBody.put("original_url", originalUrl);
      responseBody.put("transformed_url", transformedUrl);

      return ResponseEntity.ok().body(responseBody);
    }

    private String getPresignedUrl(String filename) {

      return "";
    }

  }

  public class Soratra {

    private Long id;
    private String phrase;

    public Soratra() {
    }

    public Soratra(Long id, String phrase) {
      this.id = id;
      this.phrase = phrase;
    }

  }

  public interface SoratraRepository extends JpaRepository<Soratra, Long> {
  }

}
