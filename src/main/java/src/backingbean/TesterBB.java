package src.backingbean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import src.entity.Order;
import src.inter.IServiceLocator;
import src.querystrategy.IQueryStrategy;
import src.querystrategy.IQueryStrategyManager;
import src.querystrategy.PendingDeliveryOrdersQS;

@Named
@SessionScoped
public class TesterBB implements Serializable {


	private static final long serialVersionUID = 111L;
	
	@Inject
	private IQueryStrategyManager<Order> orderQueryStrategyManager;
	@Inject
	private IServiceLocator serviceLocator;
	
	
	
	public void publish(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
	}
	
	
	public void test() {
		String msg = "Numero de ordenes pendientes de envio = " + getNumPendingDeliveryOrders();
		publish(msg);		
	}
	
	
	
	public Integer getNumPendingDeliveryOrders() {
		return getOrderQueryStrategyManager().getList().size();
	}
	
	
	
	
	public IQueryStrategyManager<Order> getOrderQueryStrategyManager() {
		if(orderQueryStrategyManager.getStrategy() == null) {
			IQueryStrategy<Order> strategy = new PendingDeliveryOrdersQS();
			strategy.setServiceLocator(getServiceLocator());
			orderQueryStrategyManager.setStrategy(strategy);
		}
		return orderQueryStrategyManager;
	}
	public void setOrderQueryStrategyManager(IQueryStrategyManager<Order> orderQueryStrategyManager) {
		this.orderQueryStrategyManager = orderQueryStrategyManager;
	}
	public IServiceLocator getServiceLocator() {
		return serviceLocator;
	}
	public void setServiceLocator(IServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
