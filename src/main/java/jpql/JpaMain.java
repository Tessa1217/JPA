package jpql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class JpaMain {
	
	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			
			Member member = new Member();
			member.setName("MEMBER 1");
			member.setStreet("Member Street");
			em.persist(member);
			
			Item item = new Item();
			item.setName("Item 1");
			item.setPrice(10000);
			item.setStockQuantity(10);
			em.persist(item);
			
			Order order = new Order();
			order.setMember(member);
			order.setOrderDate(new Date());
			order.setStatus(OrderStatus.ORDER);
			em.persist(order);
			
			OrderItem orderItem = new OrderItem();
			orderItem.setCount(2);
			orderItem.setItem(item);
			orderItem.setOrderPrice(orderItem.getItem().getPrice() * orderItem.getCount());
			orderItem.setOrder(order);
			em.persist(orderItem);
			
			order.addOrderItem(orderItem);
			List<Order> orderList = new ArrayList<>();
			orderList.add(order);
			member.setOrders(orderList);
			
			em.flush();
			em.clear();
			
			Order targetOrder = em.find(Order.class, order.getId());
			for (OrderItem orderedItem : targetOrder.getOrderItems()) {
				System.out.println("count=> " + orderedItem.getCount() + ", totalPrice=> " + orderedItem.getOrderPrice());
			}
			
			Member targetMember = em.find(Member.class, member.getId()); 
			System.out.println(targetOrder.getMember() == targetMember); // true
			
			for (Order memberOrder : targetMember.getOrders()) {
				System.out.println("status=> " + memberOrder.getStatus() + ", date=>" + memberOrder.getOrderDate());
			}
			
			tx.commit();
			
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
		
		emf.close();
	} 
	
}
