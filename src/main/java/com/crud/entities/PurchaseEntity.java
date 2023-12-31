package com.crud.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compras")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE compras SET deleted = true WHERE id=?")
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad")
    private int amount;

    @Column(name = "precio")
    private Double price;

    private Double total;

/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;*/

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "purchase_vuelo", joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "vuelo_id"))
    private List<FlightEntity> flights = new ArrayList<>();

    @Column(columnDefinition = "BOOLEAN NOT NULL DEFAULT '0'")
    private boolean deleted;
}
