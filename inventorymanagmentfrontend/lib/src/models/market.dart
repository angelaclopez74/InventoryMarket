class Market {
  Market({
    required this.id,
    required this.name,
    required this.address,
    required this.location,
  });

  final String id;
  final String name;
  final String address;
  final String location;

  factory Market.fromJson(Map<String, dynamic> json) {
    return Market(
      id: (json['id'] as String?) ?? '',
      name: (json['name'] as String?) ?? '',
      address: (json['address'] as String?) ?? '',
      location: (json['location'] as String?) ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    // El backend genera el id si no viene
    return {
      'id': id.isEmpty ? null : id,
      'name': name,
      'address': address,
      'location': location,
    };
  }
}

