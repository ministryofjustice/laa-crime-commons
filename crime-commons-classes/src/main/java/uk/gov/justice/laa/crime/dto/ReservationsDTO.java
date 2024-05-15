package uk.gov.justice.laa.crime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationsDTO {

    private Integer recordId;

    private String userName;

    private String userSession;

    private String recordName;

    private LocalDateTime reservationDate;

    private LocalDateTime expiryDate;
}
