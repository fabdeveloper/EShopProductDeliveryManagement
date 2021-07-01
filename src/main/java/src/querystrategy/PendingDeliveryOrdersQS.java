package src.querystrategy;

import java.util.List;

import src.entity.Order;

public class PendingDeliveryOrdersQS extends AbstractQueryStrategy<Order> {

	@Override
	public List<Order> executeStrategy() {
		return getServiceLocator().getOrderServices().createNamedQueryLimited("loadPendingDeliveryOrders", 100);
	}

}
