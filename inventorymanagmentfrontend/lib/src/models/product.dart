class Product {
  Product({
    required this.id,
    required this.marketId,
    required this.name,
    required this.descripcion,
    required this.fixedPrice,
    required this.qr,
  });

  final String id;
  final String marketId;
  final String name;
  final String descripcion;
  final double fixedPrice;
  final String qr;

  factory Product.fromJson(Map<String, dynamic> json) {
    final price = json['fixedPrice'];
    return Product(
      id: (json['id'] as String?) ?? '',
      marketId: (json['marketId'] as String?) ?? '',
      name: (json['name'] as String?) ?? '',
      descripcion: (json['descripcion'] as String?) ?? '',
      fixedPrice: price is num ? price.toDouble() : 0,
      qr: (json['qr'] as String?) ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    // El backend genera id/qr/marketId al crear
    return {
      'id': id.isEmpty ? null : id,
      'marketId': marketId.isEmpty ? null : marketId,
      'name': name,
      'descripcion': descripcion,
      'fixedPrice': fixedPrice,
    };
  }
}

