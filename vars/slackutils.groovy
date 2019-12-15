def send(String $channel, String $message, String $status = "success") {
    assert $channel != null
    assert $message != null
    slackSend(channel: $channel,
                iconEmoji: $status = "success" ? ':dancer:' : ':boom:',
                color: $status = "success" ? 'good' : 'danger',
                message:  $message + emoji($status)
            )
}

def emoji(String status = "success") {
    def emojis = status == "success" ?
                        [":v:", ":ok_hand:", ":clap:", ":man_dancing:", ":trollface:",
                        ":rocket:", ":ghost:", ":thumbsup:", ":tada:", ":confetti_ball:"]
                        :
                        [":thumbsdown:", ":scream_cat:", ":triumph:", ":rage:", ":bangbang:",
                        ":see_no_evil:", ":hear_no_evil:", ":speak_no_evil:", ":gun:"]
    def RandomIndex = (new Random()).nextInt(emojis.size())
    return emojis[RandomIndex]
}