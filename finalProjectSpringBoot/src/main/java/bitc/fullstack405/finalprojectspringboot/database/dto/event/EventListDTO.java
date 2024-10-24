package bitc.fullstack405.finalprojectspringboot.database.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class EventListDTO {

  private String eventPoster;
  private String eventTitle;
  private LocalDate uploadDate;
  private int maxPeople;
  private int eventAccept;
  private char isRegistrationOpen;
  private Long eventId;

  private String eventUploaderName;
  private String eventApproverName;

  private LocalDate visibleDate;
  private LocalDate invisibleDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private LocalDate startDate;
  private LocalDate endDate;

  private int totalAppliedPeople; // 신청 인원
  private int completedPeople; // 수료 인원
}
