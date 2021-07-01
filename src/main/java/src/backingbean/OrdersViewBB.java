package src.backingbean;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import src.entity.Order;
import src.inter.IServiceLocator;
import src.querystrategy.IQueryStrategy;
import src.querystrategy.IQueryStrategyManager;
import src.querystrategy.PendingDeliveryOrdersQS;
import src.querystrategy.orders.OrderQueryStrategyManager;

@Named
@SessionScoped
public class OrdersViewBB implements Serializable {

	private static final long serialVersionUID = 1000L;


	static Logger logger = Logger.getLogger(OrdersViewBB.class.getName());

	
	private String itemSel = "0";
	private Order orderseleccionada;
	
	private IQueryStrategyManager<Order> qsm;
	
	@Inject
	private IServiceLocator serviceLocator;


	public IQueryStrategyManager<Order> getQsm() {
		if(qsm == null) {
			initQsm();
		}
		return qsm;
	}
	
	public void initQsm() {
		IQueryStrategy<Order> strategy = new PendingDeliveryOrdersQS();
		strategy.setServiceLocator(getServiceLocator());
		qsm = new OrderQueryStrategyManager();
		qsm.setStrategy(strategy);
		qsm.reset();
		
		itemSel = getList().get(0).getId().toString();
		
	}
	
	

	public IServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(IServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public void setQsm(IQueryStrategyManager<Order> qsm) {
		this.qsm = qsm;
	}

	public String getItemSel() {
		return itemSel;
	}

	public void setItemSel(String itemSel) {
		this.itemSel = itemSel;
	}

	public List<Order> getList() {
		return getQsm().getList();
	}


	public Order getOrderseleccionada() {
		if(orderseleccionada == null) {
			initOrdenSeleccionada();
		}
		return orderseleccionada;
	}
	
	public void initOrdenSeleccionada() {
		publish("OrdersViewBB.initOrdenSeleccionada() ... - items= " + getList().size());
		for(Order ord : getList()) {
			publish("order.id = " + ord.getId() + ", itemSel = " + itemSel);
			if(ord.getId().compareTo(Integer.valueOf(itemSel)) == 0) {
				orderseleccionada = ord;
				publish("OrdersViewBB.initOrdenSeleccionada() - encontrado match - ");

			}
		}
	}
	
	public void resetOrdenSeleccionada() { orderseleccionada = null; }
	
	public void resetOrdenSeleccionada(String itemSel) { 
		setItemSel(itemSel);
		resetOrdenSeleccionada(); 
	}


	public void setOrderseleccionada(Order orderseleccionada) {
		this.orderseleccionada = orderseleccionada;
	}


	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		OrdersViewBB.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void publish(String msg) {
//		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
//		System.out.println(msg);
		
		logger.log(Level.INFO, msg);

	}
	
	

}
