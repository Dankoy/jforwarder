package ru.dankoy.coubconnector.coub_connector.core.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.dankoy.coubconnector.coub_connector.core.service.sheduler.SchedulerCoubProducer;


@RequiredArgsConstructor
@Controller
public class CoubLogicController {

  private final SchedulerCoubProducer schedulerCoubProducer;


  @GetMapping(path = "/api/v1/scheduler")
  public void coub() {

    schedulerCoubProducer.scheduledOperation();

  }

}
