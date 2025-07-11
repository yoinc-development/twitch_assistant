package ch.yoinc.controller.api;

import ch.yoinc.model.Assistant;
import ch.yoinc.service.SpeechToTextService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = {"http://localhost:3000", "app://."})
public class DashboardApiController {

    private final SpeechToTextService speechToTextService;

    private boolean isRecording = false;

    public DashboardApiController(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    @GetMapping("/assistants")
    public ResponseEntity<List<Assistant>> getAssistants() {
        List<Assistant> assistants = List.of(
            new Assistant("1", "Default Assistant", "A helpful assistant for your stream"),
            new Assistant("2", "Game Helper", "Helps with game-related questions")
        );
        return ResponseEntity.ok(assistants);
    }

    @PostMapping("/start-recording")
    public ResponseEntity<Void> startRecording() {
        if (!isRecording) {
            isRecording = true;
            //speechToTextService.startRecording();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop-recording")
    public ResponseEntity<String> stopRecording() {
        if (isRecording) {
            isRecording = false;
            //speechToTextService.stopRecording();
        }
        return ResponseEntity.ok("Recording stopped");
    }

    @PostMapping("/read-chat")
    public ResponseEntity<Void> readChat() {
        // TODO: Implement actual chat reading logic
        return ResponseEntity.ok().build();
    }
}
