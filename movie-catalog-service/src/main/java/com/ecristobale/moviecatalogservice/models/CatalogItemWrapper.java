package com.ecristobale.moviecatalogservice.models;

import java.util.List;

public class CatalogItemWrapper {

	private List<CatalogItem> catalogItem;

	public List<CatalogItem> getCatalogItem() {
		return catalogItem;
	}

	public void setCatalogItem(List<CatalogItem> catalogItem) {
		this.catalogItem = catalogItem;
	}
}
