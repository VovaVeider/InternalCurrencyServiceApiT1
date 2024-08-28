package org.vladimir.t1.currency.service.api.dto.fsc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.vladimir.t1.currency.service.api.entity.FSC;
import org.vladimir.t1.currency.service.api.entity.FscType;

import java.io.Serializable;

/**
 * DTO for {@link FSC}
 */
@Data
@AllArgsConstructor
@Builder
public class FSCShortInfo implements Serializable {
    Long id;
    String name;
    Long accountBalance;
    FscType fscType;
    String ownerName;
    String ownerLastname;
    String ownerSurname;
    Boolean disabled;
}