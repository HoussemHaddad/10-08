package com.gestionformation.repository;




import com.gestionformation.domain.Produit;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProduitRepository extends JpaRepository<Produit, Long>{
	@Query("select p from Produit p where p.designation like :x")
	public Page<Produit> chercher(@Param("x") String mc, Pageable pageable);
//    @Query("insert into Produit(a,b,c,d)")
//public void insert(@Param("a")Long a,@Param("b") String b, @Param("c") double c, @Param("d") int d);

//    @Query("update Produit p set p.quantite= d where p.id= a")
//    public void up(@Param("a")Long a, @Param("d") int d);
}
