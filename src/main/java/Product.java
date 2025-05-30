import java.io.Serializable;

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	private String productCode;			// Código
	private String productName;			// Nombre
	private String productLine;			// Tipo de producto
	private String productScale;		// Escala
	private String productVendor;		// Proveedor
	private String productDescription;	// Descripción
	private int quantityInStock;		// Unidades en stock
	private float buyPrice;				// Precio de compra
	private float MSRP;					// Precio recomendado de venta (Manufacturer Suggested Retail Price)
	
	public Product(String productCode, String productName, String productLine, String productScale,
			String productVendor, String productDescription, int quantityInStock, float buyPrice, float mSRP) {
		super();
		this.productCode = productCode;
		this.productName = productName;
		this.productLine = productLine;
		this.productScale = productScale;
		this.productVendor = productVendor;
		this.productDescription = productDescription;
		this.quantityInStock = quantityInStock;
		this.buyPrice = buyPrice;
		MSRP = mSRP;
	}

	public String getProductCode() {
		return productCode;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductLine() {
		return productLine;
	}

	public String getProductScale() {
		return productScale;
	}

	public String getProductVendor() {
		return productVendor;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public int getQuantityInStock() {
		return quantityInStock;
	}

	public float getBuyPrice() {
		return buyPrice;
	}

	public float getMSRP() {
		return MSRP;
	}

}
