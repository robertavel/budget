package lt.velykis.roberta.budget.app.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private UUID id;

    @NotEmpty(message = "Please enter account")
    private String name;

}
