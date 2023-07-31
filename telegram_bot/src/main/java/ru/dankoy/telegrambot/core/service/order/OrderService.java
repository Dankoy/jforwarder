package ru.dankoy.telegrambot.core.service.order;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Order;

public interface OrderService {

  List<Order> findAll();

}
