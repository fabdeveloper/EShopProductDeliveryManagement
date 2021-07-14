package src.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import src.entity.DeliveryDetailsStatusType;
import src.entity.Order;
import src.inter.IServiceLocator;
import src.jsfcompslib.util.interfaces.IProcessable;
import src.querystrategy.IQueryStrategy;
import src.querystrategy.IQueryStrategyManager;
import src.querystrategy.PendingDeliveryOrdersQS;
import src.querystrategy.orders.OrderQueryStrategyManager;

@Named
@SessionScoped
public class OrdersViewBB implements Serializable, IProcessable {

	private static final long serialVersionUID = 1000L;


	static Logger logger = Logger.getLogger(OrdersViewBB.class.getName());

	// ordenes
	private String itemSel = "0";
	private Order orderseleccionada;	
	private IQueryStrategyManager<Order> qsm;
	
	// estado de la entrega
	private DeliveryDetailsStatusType estadoEntrega = null;
	private List<DeliveryDetailsStatusType> listaDeliveryStatus = null;
	private String deliveryRemarks = null;
	
	
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
		resetOrdenSeleccionada();
//		publish("OrdersViewBB.initOrdenSeleccionada() ... - items= " + getList().size());
		for(Order ord : getList()) {
//			publish("order.id = " + ord.getId() + ", itemSel = " + itemSel);
			if(ord.getId().compareTo(Integer.valueOf(itemSel)) == 0) {
				orderseleccionada = ord;
//				publish("OrdersViewBB.initOrdenSeleccionada() - encontrado match - ");

			}
		}
	}
	
	public void resetOrdenSeleccionada() { 
		orderseleccionada = null;
		setDeliveryRemarks(null);
		setEstadoEntrega(null);		
	}
	
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
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
		System.out.println(msg);
		
		getLogger().log(Level.INFO, msg);
	}
	
	@Transactional
	public String process() {
		String msg = "Estado entrega = " + getEstadoEntrega() + ", deliveryRemarks = " + getDeliveryRemarks();
		publish(msg);
		
		// actualizar la orden
		getOrderseleccionada().getDeliveryDetails().setStatus(getEstadoEntrega());
		getOrderseleccionada().getDeliveryDetails().setRemark(getDeliveryRemarks());
		getOrderseleccionada().getDeliveryDetails().setLastModificationDate(new Date());
		
		// grabar cambios
		getServiceLocator().getOrderServices().merge(getOrderseleccionada());
		return null;
	}

	public DeliveryDetailsStatusType getEstadoEntrega() {
		if(estadoEntrega == null) {
			estadoEntrega = getOrderseleccionada().getDeliveryDetails().getStatus();
		}
		return estadoEntrega;
	}

	public void setEstadoEntrega(DeliveryDetailsStatusType estadoEntrega) {
		this.estadoEntrega = estadoEntrega;
	}

	public List<DeliveryDetailsStatusType> getListaDeliveryStatus() {
		if(listaDeliveryStatus == null) {
			listaDeliveryStatus = new ArrayList<DeliveryDetailsStatusType>(Arrays.asList(DeliveryDetailsStatusType.values()));
		}
		return listaDeliveryStatus;
	}

	public void setListaDeliveryStatus(List<DeliveryDetailsStatusType> listaDeliveryStatus) {
		this.listaDeliveryStatus = listaDeliveryStatus;
	}

	public String getDeliveryRemarks() {
		if(deliveryRemarks == null) {
			deliveryRemarks = getOrderseleccionada().getDeliveryDetails().getRemark();
		}
		return deliveryRemarks;
	}

	public void setDeliveryRemarks(String deliveryRemarks) {
		this.deliveryRemarks = deliveryRemarks;
	}
	
	

}
