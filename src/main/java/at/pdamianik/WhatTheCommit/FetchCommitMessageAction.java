package at.pdamianik.WhatTheCommit;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.ui.Refreshable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class FetchCommitMessageAction extends AnAction {
	private final ExceptionNotifier exceptionNotifier = new ExceptionNotifier();

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		final CommitMessageI commitMessage = getCommitPanel(e);

		if (commitMessage == null)
			return;

		try {
			commitMessage.setCommitMessage(fetchCommitMessage());
		} catch (IOException ioException) {
			StringBuilder stackTrace = new StringBuilder();
			for (StackTraceElement stackTraceElement : ioException.getStackTrace())
				stackTrace.append(stackTraceElement.toString()).append("\n\r");
			exceptionNotifier.notify(stackTrace.toString());
		}
	}

	private static String fetchCommitMessage() throws IOException {
		HttpURLConnection con = (HttpURLConnection) (new URL("http://whatthecommit.com/index.txt").openConnection());
		BufferedReader responseReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

		StringBuilder response = new StringBuilder();
		String line;

		while ((line = responseReader.readLine()) != null) {
			response.append(line);
			response.append("\r");
		}

		responseReader.close();
		con.disconnect();

		return response.toString();
	}

	/*
	Credit for the following method "getCommitPanel" goes to https://github.com/JanGatting.
	His source code can be found in https://github.com/JanGatting/GitCommitMessage/blob/master/src/de/gatting/scm/OpenPanelAction.java.
	The original project (https://github.com/JanGatting/GitCommitMessage) is licensed under the Apache 2.0 license
	 */

	@Nullable
	private static CommitMessageI getCommitPanel(@Nullable AnActionEvent e) {
		if (e == null) {
			return null;
		}
		Refreshable data = Refreshable.PANEL_KEY.getData(e.getDataContext());
		if (data instanceof CommitMessageI) {
			return (CommitMessageI) data;
		}
		return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.getDataContext());
	}
}
