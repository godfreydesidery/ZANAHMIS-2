/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Collection;
import com.orbix.api.reports.models.CollectionReport;

/**
 * @author Godfrey
 *
 */
public interface CollectionRepository extends JpaRepository <Collection, Long> {
	
	@Query(
			value = "SELECT\r\n" + 
					"`users`.`id` AS `user_id`,\r\n" +
					"`collections`.`item_name` AS `itemName`,\r\n" +
					"`collections`.`payment_channel` AS `paymentChannel`,\r\n" +
					"SUM(`collections`.`amount`) AS `amount`\r\n" + 
					"FROM\r\n" + 
					"`collections`\r\n" + 
					"JOIN\r\n" + 
					"`users`\r\n" + 
					"ON\r\n" + 
					"`users`.`id`=`collections`.`created_by_user_id`\r\n" + 
					"WHERE\r\n" +
					"`collections`.`created_at` BETWEEN :from AND :to\r\n" +
					"GROUP BY\r\n" + 
					"`itemName`, `paymentChannel`",
					nativeQuery = true					
			)
	List<CollectionReport> getCollectionReportGeneral(LocalDateTime from, LocalDateTime to);
	
	@Query(
			value = "SELECT\r\n" + 
					"`users`.`id` AS `user_id`,\r\n" +
					"`collections`.`item_name` AS `itemName`,\r\n" +
					"`collections`.`payment_channel` AS `paymentChannel`,\r\n" +
					"SUM(`collections`.`amount`) AS `amount`\r\n" + 
					"FROM\r\n" + 
					"`collections`\r\n" + 
					"JOIN\r\n" + 
					"`users`\r\n" + 
					"ON\r\n" + 
					"`users`.`id`=`collections`.`created_by_user_id`\r\n" + 
					"WHERE\r\n" +
					"`collections`.`created_at` BETWEEN :from AND :to AND `users`.`id`=:userId\r\n" +
					"GROUP BY\r\n" + 
					"`itemName`, `paymentChannel`",
					nativeQuery = true					
			)
	List<CollectionReport> getCollectionReportByCashier(LocalDateTime from, LocalDateTime to, Long userId);
	
	
}
