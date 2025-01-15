package org.ms.facture_servicePJ.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {
	private long id;
	private String name;
	private String email;
	private String phone;
}
