package IntentClassification;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/intent")
@RequiredArgsConstructor
public class IntentController {

    private final IntentService intentService;
    private final ClientService clientService;

    @PostMapping("/getIntent")
    public String getIntent(@RequestBody String prompt) {
        System.out.println("Prompt: " + prompt);
        return "hello";

    }

    @GetMapping("/intent2")
    public String getAllClients(@PathVariable String prompt) {
        System.out.println("Prompt: " + prompt);
        return "hello" + prompt;


    }

}
