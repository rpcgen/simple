def process() {

    def result = [:]

    comments.each({

        def parent = it
        result[parent.getId()] = parent.getUpVotes()

        comments.each({
            if (it.getInResponseTo() != null && it.getInResponseTo().getId() == parent.getId())
                result[parent.getId()] += 5
        })
    })

    return result
}

process()