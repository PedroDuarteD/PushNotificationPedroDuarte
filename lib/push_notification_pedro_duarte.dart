
import 'dart:async';

import 'package:flutter/services.dart';

class PushNotificationPedroDuarte {
  static const MethodChannel _channel = MethodChannel('push_notification_pedro_duarte');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future  notification_push(String titulo, String texto) async {
    await _channel.invokeMapMethod('notification',
        {"titulo" :titulo,"texto" :texto});

  }

}
