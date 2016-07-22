package jp.muto;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;

@CsvEntity
public class NishiSightseeing {
	@CsvColumn(name = "名所・スポット名")
	private String name;
	@CsvColumn(name = "住所")
	private String address;
	@CsvColumn(name = "X座標")
	private double positionX;
	@CsvColumn(name = "Ｙ座標")
	private double positionY;
	@CsvColumn(name = "解説")
	private String explanation;
	@CsvColumn(name = "更新日")
	private String lastUpDate;

	public NishiSightseeing() {
		// TODO Auto-generated constructor stub
	}

	public NishiSightseeing(String name, String address, double positionX, double positionY, String explanation,
			String lastUpDate) {
		super();
		this.name = name;
		this.address = address;
		this.positionX = positionX;
		this.positionY = positionY;
		this.explanation = explanation;
		this.lastUpDate = lastUpDate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the positionX
	 */
	public double getPositionX() {
		return positionX;
	}

	/**
	 * @return the positionY
	 */
	public double getPositionY() {
		return positionY;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @return the lastUpDate
	 */
	public String getLastUpDate() {
		return lastUpDate;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param positionX
	 *            the positionX to set
	 */
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	/**
	 * @param positionY
	 *            the positionY to set
	 */
	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	/**
	 * @param explanation
	 *            the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * @param lastUpDate
	 *            the lastUpDate to set
	 */
	public void setLastUpDate(String lastUpDate) {
		this.lastUpDate = lastUpDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NishiSightseeing [name=" + name + ", address=" + address + ", positionX=" + positionX + ", positionY="
				+ positionY + ", explanation=" + explanation + ", lastUpDate=" + lastUpDate + "]";
	}

}
