package com.example.demo.Reoisitory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.DBManager;
import com.example.demo.Domain.Category;
import com.example.demo.Domain.Item;

/**
 * @author yamasakimanahito
 *
 */
@Repository
public class ItemRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final ResultSetExtractor<List<Item>> ITEM_RESULT_SET_EXTRACTOR = (rs) -> {
		List<Item> itemList = new LinkedList<>();
		List<Category> categoryList = null;

		int depth = findCategoryNameAll();
		long beforeItemId = -1;

		while (rs.next()) {
			int nowItemId = rs.getInt("id");

			if (beforeItemId != nowItemId) {
				Item item = new Item();
				item.setId(nowItemId);
				item.setConditionId(rs.getInt("condition"));
				item.setName(rs.getString("name"));
				item.setPrice(rs.getDouble("price"));
				item.setCategory(rs.getInt("category"));
				item.setBrandName(rs.getString("brand"));
				item.setShipping(rs.getInt("shipping"));
				item.setDescription(rs.getString("description"));
				categoryList = new LinkedList<Category>();
				item.setCategoryList(categoryList);
				itemList.add(item);
			}

			for (int i = 1; i <= depth; i++) {

				Category category = new Category();
				category.setId(rs.getInt("category" + (i) + "_id"));
				category.setName(rs.getString("category" + (i) + "_name"));
				if (!(rs.getInt("category" + (i) + "_id") == 0)) {
					categoryList.add(category);
				}

			}
			beforeItemId = nowItemId;
		}
		return itemList;

	};
	
	private static final RowMapper<Item> ITEM_ROW_MAPPER = new BeanPropertyRowMapper<>(Item.class);

	/**
	 * 商品の全件検索を行う.
	 * 
	 * @return 商品が全件入ったList
	 * @throws SQLException
	 */
	public List<Item> findAll() throws SQLException {
		int maxDepth = findCategoryNameAll();
		String baseSql = "select i.id,i.condition,i.name,i.price,i.category,i.brand,i.shipping,i.description,";
		StringBuilder selectbs = new StringBuilder();
		StringBuilder joinbs = new StringBuilder();
		for (int i = maxDepth; i > 0; i--) {
			// select部分の繰り返し処理
			if (i == 1) {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name ");
			} else {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name,");
			}
			// left outer join 部分の繰り返し処理
			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
					+ ".id ");
		}
		String resultSql = baseSql + selectbs + "from items as i left outer join category as c" + maxDepth
				+ " on i.category=c" + maxDepth + ".id" + joinbs + "order by i.id limit 300 offset 0";

		List<Item> itemList = template.query(resultSql, ITEM_RESULT_SET_EXTRACTOR);
		return itemList;
	}
	
	/**
	 * カテゴリー名から商品の検索を行う.
	 * 
	 * @param name カテゴリー名
	 * @return 検索された商品一覧
	 * @throws SQLException
	 */
	public List<Item> findByCategoryId(Integer id) throws SQLException {
		int maxDepth = findCategoryNameAll();
		String baseSql = "select i.id,i.condition,i.name,i.price,i.category,i.brand,i.shipping,i.description,";
		StringBuilder selectbs = new StringBuilder();
		StringBuilder joinbs = new StringBuilder();
		StringBuilder wherebs = new StringBuilder();
		for (int i = maxDepth; i > 0; i--) {
			// select部分の繰り返し処理
			if (i == 1) {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name ");
			} else {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name,");
			}
			// left outer join 部分の繰り返し処理
			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
					+ ".id ");
			if (i == 1) {
				wherebs.append("c" + i + ".id=:id");

			} else {
				wherebs.append("c" + i + ".id=:id or ");
			}
		}
		String resultSql = baseSql + selectbs + "from items as i left outer join category as c" + maxDepth
				+ " on i.category=c" + maxDepth + ".id" + joinbs + "where " + wherebs + " and i.id<=100"
				+ " order by i.id limit 300 offset 0;";
		System.out.println(resultSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		System.out.println(id);

		List<Item> itemList = template.query(resultSql, param, ITEM_RESULT_SET_EXTRACTOR);
		return itemList;
	}

	/**
	 * 主キーから商品の検索を行う.
	 * 
	 * @param id 商品ID
	 * @return Idによって検索された商品
	 * @throws SQLException
	 */
	public List<Item> load(Integer id) throws SQLException {
		int maxDepth = findCategoryNameAll();
		String baseSql = "select i.id,i.condition,i.name,i.price,i.category,i.brand,i.shipping,i.description,";
		StringBuilder selectbs = new StringBuilder();
		StringBuilder joinbs = new StringBuilder();
		for (int i = maxDepth; i > 0; i--) {
			// select部分の繰り返し処理
			if (i == 1) {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name ");
			} else {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name,");
			}
			// left outer join 部分の繰り返し処理
			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
					+ ".id ");
		}
		String resultSql = baseSql + selectbs + "from items as i left outer join category as c" + maxDepth
				+ " on i.category=c" + maxDepth + ".id" + joinbs + "where i.id=:id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		List<Item> itemList = template.query(resultSql, param, ITEM_RESULT_SET_EXTRACTOR);
		return itemList;

	}

	/**
	 * 区切られているカテゴリーの数を算出する.
	 * 
	 * @return カテゴリーの最大数
	 * @throws SQLException
	 */
	public static int findCategoryNameAll() throws SQLException {
		Statement stmt = null;
		Connection conn = null;

		conn = DBManager.createConnection(); // SQLに接続

		// コピー元のテーブルからデータを取得
		stmt = conn.createStatement(); // 新しいStatementオブジェクトを生成する
		ResultSet rs = stmt.executeQuery("SELECT max(depth) as depth from category");
		int maxDepth = 0;
		while (rs.next()) {
			maxDepth = rs.getInt("depth");
		}
		System.out.println(maxDepth);
		return maxDepth;

	}

	/**
	 * 商品名、カテゴリー名、ブランド名から商品の検索を行う.
	 * 
	 * @param name               商品名
	 * @param parentId           親ID
	 * @param childId            子ID
	 * @param grandChildcategory 孫ID
	 * @param brand              ブランド名
	 * @return 条件によって検索された商品
	 * @throws SQLException
	 */
	/**
	 * @param name
	 * @param parentId
	 * @param childId
	 * @param grandChildcategory
	 * @param brand
	 * @return
	 * @throws SQLException
	 */
	public List<Item> findByNameAndCategoryIdAndBrand(String name, Integer parentId, Integer childId,
			Integer grandChildcategory, String brand) throws SQLException {
		int maxDepth = findCategoryNameAll();
		String baseSql = "select i.id,i.condition,i.name,i.price,i.category,i.brand,i.shipping,i.description,";
		StringBuilder selectbs = new StringBuilder();
		StringBuilder joinbs = new StringBuilder();
		for (int i = maxDepth; i > 0; i--) {
			// select部分の繰り返し処理
			if (i == 1) {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name ");
			} else {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name,");
			}
			// left outer join 部分の繰り返し処理
			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
					+ ".id ");
		}
		String nameSql = null;
		String categorySql = null;
		String brandSql = null;

		if (name.equals("")) {
			nameSql = "";
		} else {
			nameSql = " and i.name ilike :name ";
		}

		if (parentId == 0) {
			categorySql = "";
		} else if (childId == 0) {
			categorySql = "and c3.id=:parentId ";
		} else if (grandChildcategory == 0) {
			categorySql = "and c4.id=:childId ";
		} else {
			categorySql = "and c5.id=:categoryId ";
		}

		if (brand.equals("")) {
			brandSql = " and i.brand ilike :brand ";
		} else {
			brandSql = " and i.brand ilike :brand ";
		}
		String resultSql = baseSql + selectbs + "from items as i left outer join category as c" + maxDepth
				+ " on i.category=c" + maxDepth + ".id" + joinbs + "where i.id>=0" + nameSql + categorySql + brandSql
				+ "order by i.id limit 300 offset 0;";
		
		System.out.println(resultSql);

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
				.addValue("parentId", parentId).addValue("childId", childId).addValue("categoryId", grandChildcategory)
				.addValue("brand", "%" + brand + "%");

		List<Item> itemList = template.query(resultSql, param, ITEM_RESULT_SET_EXTRACTOR);
		return itemList;
	}

	/**
	 * 商品の更新を行う.
	 * 
	 * @param item 更新対象の商品
	 */
	public void update(Item item) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		String sql = "update items set name=:name,price=:price,category=:category,brand=:BrandName,condition=:conditionId,description=:description where id=:id;";

		template.update(sql, param);
	}

	/**
	 * 商品全件一覧のページングを行う.
	 * 
	 * @param offset
	 * @return ページングの条件によって検索された商品一覧
	 * @throws SQLException
	 */
	public List<Item> paging(Integer offset) throws SQLException {
		int maxDepth = findCategoryNameAll();
		String baseSql = "select i.id,i.condition,i.name,i.price,i.category,i.brand,i.shipping,i.description,";
		StringBuilder selectbs = new StringBuilder();
		StringBuilder joinbs = new StringBuilder();
		for (int i = maxDepth; i > 0; i--) {
			// select部分の繰り返し処理
			if (i == 1) {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name ");
			} else {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name,");
			}
			// left outer join 部分の繰り返し処理
			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
					+ ".id ");
		}
		String resultSql = baseSql + selectbs + "from items as i left outer join category as c" + maxDepth
				+ " on i.category=c" + maxDepth + ".id" + joinbs + "order by i.id limit 300 offset :offset";

		SqlParameterSource param = new MapSqlParameterSource().addValue("offset", offset);

		List<Item> itemList = template.query(resultSql, param, ITEM_RESULT_SET_EXTRACTOR);
		return itemList;
	}

	/**
	 * 商品検索が行われた時のページングを行う.
	 * 
	 * @param name               商品名
	 * @param parentId           親ID
	 * @param childId            子ID
	 * @param grandChildcategory 孫ID
	 * @param brand              ブランド名
	 * @param offset
	 * @return ページングの条件によって検索された商品一覧
	 * @throws SQLException
	 */
	public List<Item> findByNameAndCategoryIdAndBrandPaging(String name, Integer parentId, Integer childId,
			Integer grandChildcategory, String brand, Integer offset) throws SQLException {
		int maxDepth = findCategoryNameAll();
		String baseSql = "select i.id,i.condition,i.name,i.price,i.category,i.brand,i.shipping,i.description,";
		StringBuilder selectbs = new StringBuilder();
		StringBuilder joinbs = new StringBuilder();
		for (int i = maxDepth; i > 0; i--) {
			// select部分の繰り返し処理
			if (i == 1) {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name ");
			} else {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name,");
			}
			// left outer join 部分の繰り返し処理
			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
					+ ".id ");
		}
		String nameSql = null;
		String categorySql = null;
		String brandSql = null;

		if (name.equals("")) {
			nameSql = "";
		} else {
			nameSql = " and i.name ilike :name ";
		}

		if (parentId == 0) {
			categorySql = "";
		} else if (childId == 0) {
			categorySql = " and c3.id=:parentId ";
		} else if (grandChildcategory == 0) {
			categorySql = " and c4.id=:childId ";
		} else {
			categorySql = " and c5.id=:categoryId ";
		}

		if (brand.equals("")) {
			brandSql = "";
		} else {
			brandSql = " and i.brand ilike :brand ";
		}
		String resultSql = baseSql + selectbs + "from items as i left outer join category as c" + maxDepth
				+ " on i.category=c" + maxDepth + ".id" + joinbs + "where i.id>=0" + nameSql + categorySql + brandSql
				+ "order by id limit 300 offset :offset;";

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
				.addValue("parentId", parentId).addValue("childId", childId).addValue("categoryId", grandChildcategory)
				.addValue("brand", "%" + brand + "%").addValue("offset", offset);

		List<Item> itemList = template.query(resultSql, param, ITEM_RESULT_SET_EXTRACTOR);
		return itemList;
	}
	
//	public List<Item> findByCategoryIdPaging(Integer id,Integer offset) throws SQLException{
//		int maxDepth = findCategoryNameAll();
//		String baseSql = "select i.id,i.condition,i.name,i.price,i.category,i.brand,i.shipping,i.description,";
//		StringBuilder selectbs = new StringBuilder();
//		StringBuilder joinbs = new StringBuilder();
//		StringBuilder wherebs = new StringBuilder();
//		for (int i = maxDepth; i > 0; i--) {
//			// select部分の繰り返し処理
//			if (i == 1) {
//				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
//						+ "_Name ");
//			} else {
//				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
//						+ "_Name,");
//			}
//			// left outer join 部分の繰り返し処理
//			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
//					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
//					+ ".id ");
//			if (i == 1) {
//				wherebs.append("c" + i + ".id=:id");
//
//			} else {
//				wherebs.append("c" + i + ".id=:id or ");
//			}
//		}
//		String resultSql = baseSql + selectbs + "from items as i left outer join category as c" + maxDepth
//				+ " on i.category=c" + maxDepth + ".id" + joinbs + "where " + wherebs + " and i.id<=100"
//				+ " order by i.id limit 300 offset :offset;";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("offset", offset);
//		System.out.println(id);
//
//		List<Item> itemList = template.query(resultSql, param, ITEM_RESULT_SET_EXTRACTOR);
//		return itemList;
//	}

	/**
	 * 商品の追加を行う.
	 * 
	 * @param item 追加対象の商品
	 */
	public void Insert(Item item) {
		if (item.getId() == null) {
			SqlParameterSource param = new BeanPropertySqlParameterSource(item);
			String sql = "insert into items(name,price,category,brand,condition,description)values(:name,:price,:category,:BrandName,:conditionId,:description)";
			template.update(sql, param);
		}
	}

	/**
	 * 商品の総数を算出する.
	 * 
	 * @return 全商品の合計数を表すItemList
	 */
	public List<Item> ItemAmount() {
		String sql = "select from items;";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 検索した商品の総数を算出する.
	 * 
	 * @param name               商品名
	 * @param parentId           親ID
	 * @param childId            子ID
	 * @param grandChildcategory 孫ID
	 * @param brand              ブランド名
	 * @return 検索された商品の合計数を表すItemList
	 * @throws SQLException
	 */
	public List<Item> searchItemAmount(String name, Integer parentId, Integer childId,
			Integer grandChildcategory, String brand) throws SQLException{
		int maxDepth = findCategoryNameAll();
		StringBuilder joinbs = new StringBuilder();
		for (int i = maxDepth; i > 0; i--) {
			// left outer join 部分の繰り返し処理
			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
					+ ".id ");
		}
		String nameSql = null;
		String categorySql = null;
		String brandSql = null;

		if (name.equals("")) {
			nameSql = "";
		} else {
			nameSql = " and i.name ilike :name ";
		}

		if (parentId == 0) {
			categorySql = "";
		} else if (childId == 0) {
			categorySql = "and c3.id=:parentId ";
		} else if (grandChildcategory == 0) {
			categorySql = "and c4.id=:childId ";
		} else {
			categorySql = "and c5.id=:categoryId ";
		}

		if (brand.equals("")) {
			brandSql = " and i.brand ilike :brand ";
		} else {
			brandSql = " and i.brand ilike :brand ";
		}
		String resultSql ="select from items as i left outer join category as c" + maxDepth
				+ " on i.category=c" + maxDepth + ".id" + joinbs + "where i.id>=0 " + nameSql + categorySql + brandSql
				+ "order by i.id;";

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
				.addValue("parentId", parentId).addValue("childId", childId).addValue("categoryId", grandChildcategory)
				.addValue("brand", "%" + brand + "%");

		List<Item> itemList = template.query(resultSql, param, ITEM_ROW_MAPPER);
		return itemList;
		
	}
	
	public List<Item> searchByCategoryItemAmoount(Integer id) throws SQLException{
		int maxDepth = findCategoryNameAll();
		StringBuilder joinbs = new StringBuilder();
		StringBuilder wherebs = new StringBuilder();
		for (int i = maxDepth; i > 0; i--) {
			// left outer join 部分の繰り返し処理
			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
					+ ".id ");
			if (i == 1) {
				wherebs.append("c" + i + ".id=:id");

			} else {
				wherebs.append("c" + i + ".id=:id or ");
			}
		}
		String resultSql ="select from items as i left outer join category as c" + maxDepth
				+ " on i.category=c" + maxDepth + ".id" + joinbs + "where " + wherebs + " and i.id<=100"
				+ " order by i.id;";
		System.out.println(resultSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		System.out.println(id);

		List<Item> itemList = template.query(resultSql, param,ITEM_ROW_MAPPER );
		return itemList;

		
	}

	public List<Item> findByCategoryIdPaging(Integer id, Integer offset) throws SQLException {
		int maxDepth = findCategoryNameAll();
		String baseSql = "select i.id,i.condition,i.name,i.price,i.category,i.brand,i.shipping,i.description,";
		StringBuilder selectbs = new StringBuilder();
		StringBuilder joinbs = new StringBuilder();
		StringBuilder wherebs = new StringBuilder();
		for (int i = maxDepth; i > 0; i--) {
			// select部分の繰り返し処理
			if (i == 1) {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name ");
			} else {
				selectbs.append("c" + i + ".depth,c" + i + ".id as category" + i + "_id,c" + i + ".name as category" + i
						+ "_Name,");
			}
			// left outer join 部分の繰り返し処理
			joinbs.append(" left outer join category_path as cp" + i + " on cp" + i + ".decendent=c" + i + ".id and cp"
					+ i + ".depth=1 left outer join category as c" + (i - 1) + " on cp" + i + ".ancestor=c" + (i - 1)
					+ ".id ");
			if (i == 1) {
				wherebs.append("c" + i + ".id=:id");

			} else {
				wherebs.append("c" + i + ".id=:id or ");
			}
		}
		String resultSql = baseSql + selectbs + "from items as i left outer join category as c" + maxDepth
				+ " on i.category=c" + maxDepth + ".id" + joinbs + "where " + wherebs + " and i.id<=100"
				+ " order by i.id limit 300 offset :offset;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("offset", offset);
		System.out.println(id);

		List<Item> itemList = template.query(resultSql, param, ITEM_RESULT_SET_EXTRACTOR);
		return itemList;
	}
}