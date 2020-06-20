package at.pdamianik.WhatTheCommit;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;

public class ExceptionNotifier {
	private final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("What The Commit errors", NotificationDisplayType.BALLOON, true);

	public Notification notify(String content) {
		final Notification notification = NOTIFICATION_GROUP.createNotification(content, NotificationType.ERROR);
		notification.notify();
		return notification;
	}
}
