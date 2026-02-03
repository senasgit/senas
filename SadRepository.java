package com.ecc.eicom.repository;
import com.ecc.eicom.dbconnection.JdbcConfiguration;
import com.ecc.eicom.entity.DeclarationNumber;
import com.ecc.eicom.entity.Item;
import com.ecc.eicom.entity.Sad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SadRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Sad> getbydecnumber(Integer year, String office, String seri, Number nbr) {

        jdbcTemplate = new JdbcTemplate(JdbcConfiguration.OracleDataSource());

        String query =
                "SELECT " +
                        "s.ast_ayr, " +
                        "s.ide_cuo_cod, " +
                        "s.reg_ser, " +
                        "s.reg_nbr, " +
                        "s.reg_dat, " +
                        "s.ast_dat," +
                        "s.sta," +
                        "s.con_nam, " +
                        "s.con_cod " +
                        "FROM twm_mod_trn.sad s " +
                        "WHERE s.ast_ayr = ? " +
                        "AND s.ide_cuo_cod = ? " +
                        "AND s.reg_ser = ? " +
                        "AND s.reg_nbr = ?";

        List<Sad> sads = new ArrayList<>();

        RowMapper<Sad> rowMapper = (rs, rowNum) -> {
            Sad sad = mapSad(rs);

            // ADD to list
            sads.add(sad);

            return sad;
        };

        // Execute query
        jdbcTemplate.query(query, rowMapper, year, office, seri, nbr);

        // return list (fixes your error)
        return sads;
    }

    private Sad mapSad(ResultSet rs) throws SQLException {
        Sad sad = new Sad();
        sad.setAst_ayr(rs.getInt("ast_ayr"));
        sad.setIde_cuo_cod(rs.getString("ide_cuo_cod"));
        sad.setReg_ser(rs.getString("reg_ser"));
        sad.setReg_nbr(rs.getInt("reg_nbr"));
        sad.setReg_dat(rs.getDate("reg_dat"));
        sad.setAst_dat(rs.getDate("ast_dat"));
        sad.setSta(rs.getString("sta"));
        sad.setCon_nam(rs.getString("con_nam"));
        sad.setCon_cod(rs.getString("con_cod"));

       /* sad.setItems(new ArrayList<>());*/

        return sad;

    }

    private List<Item> fetchItems(DeclarationNumber dec) {

        jdbcTemplate = new JdbcTemplate(JdbcConfiguration.OracleDataSource());

        String query =
                "SELECT ast_ayr, ide_cuo_cod, reg_ser, reg_nbr " +
                        "FROM twm_mod_trn.sad " +
                        "WHERE ast_ayr = ? " +
                        "AND ide_cuo_cod = ? " +
                        "AND reg_ser = ?" +
                        "AND reg_nbr = ?";

        List<Item> items = new ArrayList<>();

        RowMapper<Item> itemRowMapper = (rs, rowNum) -> {
            Item item = new Item();
            item.setAst_ayr(rs.getInt("ast_ayr"));
            item.setIde_cuo_cod(rs.getString("ide_cuo_cod"));
            item.setReg_ser(rs.getString("reg_ser"));
            item.setReg_nbr(rs.getInt("reg_nbr"));

            items.add(item);
            return item;
        };

        jdbcTemplate.query(query, itemRowMapper,
                dec.getAst_ayr(),
                dec.getIde_cuo_cod(),
                dec.getReg_ser(),
                dec.getReg_nbr()
        );

        return items;
    }
}



















