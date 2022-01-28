import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:push_notification_pedro_duarte/push_notification_pedro_duarte.dart';

void main() {
  const MethodChannel channel = MethodChannel('push_notification_pedro_duarte');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await PushNotificationPedroDuarte.platformVersion, '42');
  });
}
