package com.Kilari.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BankDetails {


   private  String accountNumber;
   private  String accountHolderName;
   private  String ifcsCode;
}
