package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Page;
import be.thomasmore.be.websitelientjes.models.Symbol;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SymbolRepository extends CrudRepository<Symbol, Integer> {

    @Query("select s from Symbol s join s.pages pages where :page in (pages)")
    List<Symbol> getSymbolByPage(@Param("page") Page page);

    @Query("select s from Symbol s where s.referenceName like :referenceName")
    Symbol getSymbolByReferenceName(@Param("referenceName") String referenceName);
}
