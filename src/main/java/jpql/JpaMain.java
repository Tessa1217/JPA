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
			
			// 회원 생성
			Member member = new Member();
			member.setName("MEMBER 1");
			member.setStreet("Member Street");
			em.persist(member);
			
			// 아이템 생성
			Item bike = new Item();
			bike.setName("Bike");
			bike.setPrice(80000);
			bike.setStockQuantity(10);
			em.persist(bike);
			
			Item motorcycle = new Item();
			motorcycle.setName("Motor Cycle");
			motorcycle.setPrice(900000);
			motorcycle.setStockQuantity(10);
			em.persist(motorcycle);
			
			Item car = new Item();
			car.setName("Car");
			car.setPrice(10000000);
			car.setStockQuantity(10);
			em.persist(car);
			
			// 카테고리 생성
			Category transportation = new Category();
			transportation.setName("교통수단");
			em.persist(transportation);
			
			Category twoTire = new Category();
			twoTire.setName("두 바퀴 교통수단");
			twoTire.setParent(transportation);
			twoTire.addItem(bike);
			twoTire.addItem(motorcycle); // 두 바퀴 교통수단에 bike, motorcycle 추가
			em.persist(twoTire);
			
			Category fourTire = new Category();
			fourTire.setName("네 바퀴 교통수단");
			fourTire.setParent(transportation);
			fourTire.addItem(car); // 네 바퀴 교통수단에 car 추가
			em.persist(fourTire);
			
			transportation.addChildCategory(twoTire);
			transportation.addChildCategory(fourTire); // 교통수단 카테고리에 자식 카테고리 추가
			
			// 부모 자식 카테고리 불러오기
			Category parentCategory = em.find(Category.class, transportation.getId());
			for (Category childCategory : parentCategory.getChildList()) {
				System.out.println("카테고리명: " + childCategory.getName());
				for (Item cateItem : childCategory.getItems()) {
					System.out.println("카테고리 아이템: " + cateItem.getName() + ", 재고: " + cateItem.getStockQuantity());
				}
			}
			
			// 주문 생성
			Order order = new Order();
			order.setMember(member);
			order.setOrderDate(new Date());
			order.setStatus(OrderStatus.ORDER);
			em.persist(order);
			
			// 주문 아이템 생성
			OrderItem orderItem = new OrderItem();
			orderItem.setCount(2);
			orderItem.setItem(car);
			orderItem.setOrderPrice(orderItem.getItem().getPrice() * orderItem.getCount());
			orderItem.setOrder(order);
			em.persist(orderItem);
			
			OrderItem orderItem2 = new OrderItem();
			orderItem2.setCount(1);
			orderItem2.setItem(bike);
			orderItem2.setOrderPrice(orderItem2.getItem().getPrice() * orderItem2.getCount());
			orderItem2.setOrder(order);
			em.persist(orderItem2);
			
			// 아이템 재고 수량 변경하기
			car.setStockQuantity(car.getStockQuantity() - orderItem.getCount());
			bike.setStockQuantity(bike.getStockQuantity() - orderItem2.getCount());
			
			order.addOrderItem(orderItem);
			order.addOrderItem(orderItem2);
			
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
			
			// 재고 수량 변경됐는지 확인
			Category parentCategory2 = em.find(Category.class, transportation.getId());
			for (Category childCategory : parentCategory2.getChildList()) {
				System.out.println("카테고리명: " + childCategory.getName());
				for (Item cateItem : childCategory.getItems()) {
					System.out.println("카테고리 아이템: " + cateItem.getName() + ", 재고: " + cateItem.getStockQuantity());
				}
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
